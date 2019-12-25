package com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseRecipeIdentityAndDurationList extends
        UseCaseInteractor<UseCaseRecipeIdentityAndDurationList.Request,
                UseCaseRecipeIdentityAndDurationList.Response> {

    public enum RecipeFilter {
        ALL,
        FAVORITE
    }

    public enum ResultStatus {
        DATA_LOADING_ERROR,
        DATA_NOT_AVAILABLE,
        RESULT_OK
    }

    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;

    private LinkedHashMap<String, RecipeIdentityEntity> recipeIdentities = new LinkedHashMap<>();
    private LinkedHashMap<String, RecipeDurationEntity> recipeDurations = new LinkedHashMap<>();
    private List<ListItemModel> recipeListItemModels = new ArrayList<>();

    public UseCaseRecipeIdentityAndDurationList(RepositoryRecipeIdentity identityRepository,
                                                RepositoryRecipeDuration durationRepository) {
        this.recipeIdentityRepository = identityRepository;
        this.recipeDurationRepository = durationRepository;
    }

    @Override
    protected void execute(Request request) {
        if (request.getFilter() == RecipeFilter.ALL) {
            getAllRecipes();
        } else if (request.getFilter() == RecipeFilter.FAVORITE)
            getFavorites();
    }

    private void getAllRecipes() {
        getRecipeIdentityEntities();
    }

    private void getRecipeIdentityEntities() {
        recipeIdentityRepository.getAll(new DataSource.GetAllCallback<RecipeIdentityEntity>() {
            @Override
            public void onAllLoaded(List<RecipeIdentityEntity> entities) {
                for (RecipeIdentityEntity entity : entities) {
                    recipeIdentities.put(entity.getId(), entity);
                }
                loadDurationEntities();
            }

            @Override
            public void onDataNotAvailable() {
                returnEmptyList();
            }
        });
    }

    private void loadDurationEntities() {
        recipeDurationRepository.getAll(new DataSource.GetAllCallback<RecipeDurationEntity>() {
            @Override
            public void onAllLoaded(List<RecipeDurationEntity> entities) {
                for (RecipeDurationEntity entity : entities) {
                    recipeDurations.put(entity.getId(), entity);
                }
                mergeLists();
            }

            @Override
            public void onDataNotAvailable() {
                returnEmptyList();
            }
        });
    }

    private void getFavorites() {
        // TODO
    }

    private void returnEmptyList() {
        Response model = new Response(ResultStatus.DATA_NOT_AVAILABLE, recipeListItemModels);
        getUseCaseCallback().onError(model);
    }

    private void mergeLists() {
        recipeIdentities.forEach((recipeId, recipeIdentity) -> {
            RecipeDurationEntity durationEntity = recipeDurations.get(recipeId);
            ListItemModel model = new ListItemModel(
                    recipeId,
                    recipeIdentity.getTitle(),
                    recipeIdentity.getDescription(),
                    durationEntity.getPrepTime(),
                    durationEntity.getCookTime(),
                    (durationEntity.getPrepTime() + durationEntity.getCookTime())
            );
            recipeListItemModels.add(model);
        });
        returnResults();
    }

    private void returnResults() {
        Response model = new Response(ResultStatus.RESULT_OK, recipeListItemModels);
        getUseCaseCallback().onSuccess(model);
    }

    public static final class ListItemModel {
        @Nonnull
        private final String recipeId;
        @Nonnull
        private final String recipeName;
        private final String recipeDescription;
        private final int prepTime;
        private final int cookTime;
        private final int totalTime;

        public ListItemModel(@Nonnull String recipeId,
                             @Nonnull String recipeName,
                             String recipeDescription,
                             int prepTime,
                             int cookTime,
                             int totalTime) {
            this.recipeId = recipeId;
            this.recipeName = recipeName;
            this.recipeDescription = recipeDescription;
            this.prepTime = prepTime;
            this.cookTime = cookTime;
            this.totalTime = totalTime;
        }

        @Nonnull
        public String getRecipeId() {
            return recipeId;
        }

        @Nonnull
        public String getRecipeName() {
            return recipeName;
        }

        public String getRecipeDescription() {
            return recipeDescription;
        }

        public int getPrepTime() {
            return prepTime;
        }

        public int getCookTime() {
            return cookTime;
        }

        public int getTotalTime() {
            return totalTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListItemModel that = (ListItemModel) o;
            return prepTime == that.prepTime &&
                    cookTime == that.cookTime &&
                    totalTime == that.totalTime &&
                    recipeId.equals(that.recipeId) &&
                    recipeName.equals(that.recipeName) &&
                    recipeDescription.equals(that.recipeDescription);
        }

        @Override
        public int hashCode() {
            return Objects.hash(recipeId, recipeName, recipeDescription, prepTime, cookTime, totalTime);
        }
    }

    public static final class Request implements UseCaseInteractor.Request {
        @Nonnull
        private final RecipeFilter filter;

        public Request(@Nonnull RecipeFilter filter) {
            this.filter = filter;
        }

        @Nonnull
        public RecipeFilter getFilter() {
            return filter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request that = (Request) o;
            return filter == that.filter;
        }

        @Override
        public int hashCode() {
            return Objects.hash(filter);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "filter=" + filter +
                    '}';
        }
    }

    public class Response implements UseCaseInteractor.Response {
        @Nonnull
        private final ResultStatus resultStatus;

        @Nonnull
        private final List<ListItemModel> recipeListItemModels;

        public Response(
                @Nonnull ResultStatus resultStatus,
                @Nonnull List<ListItemModel> recipeListItemModels) {
            this.resultStatus = resultStatus;
            this.recipeListItemModels = recipeListItemModels;
        }

        @Nonnull
        public ResultStatus getResultStatus() {
            return resultStatus;
        }

        @Nonnull
        public List<ListItemModel> getRecipeListItemModels() {
            return recipeListItemModels;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response that = (Response) o;
            return resultStatus == that.resultStatus &&
                    recipeListItemModels.equals(that.recipeListItemModels);
        }

        @Override
        public int hashCode() {
            return Objects.hash(resultStatus, recipeListItemModels);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Response{" +
                    "resultStatus=" + resultStatus +
                    ", recipeListItemModels=" + recipeListItemModels +
                    '}';
        }
    }
}
