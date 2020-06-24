package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipePortions
        extends
        UseCaseElement<
                RepositoryRecipePortions,
                RecipePortionsPersistenceModel,
                RecipePortions.DomainModel> {

    private static final String TAG = "tkm-" + RecipePortions.class.getSimpleName() + ": ";

    protected static final class DomainModel
            implements
            UseCaseDomainModel {
        private int servings;
        private int sittings;

        public DomainModel() {
        }

        public DomainModel(int servings, int sittings) {
            this.servings = servings;
            this.sittings = sittings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;

            DomainModel that = (DomainModel) o;

            if (servings != that.servings) return false;
            return sittings == that.sittings;
        }

        @Override
        public int hashCode() {
            int result = servings;
            result = 31 * result + sittings;
            return result;
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    '}';
        }
    }

    public enum FailReason
            implements
            FailReasons {
        SERVINGS_TOO_LOW(350),
        SERVINGS_TOO_HIGH(351),
        SITTINGS_TOO_LOW(352),
        SITTINGS_TOO_HIGH(353);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason s : FailReason.values())
                options.put(s.id, s);
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    static final int MIN_SERVINGS = 1;
    static final int MIN_SITTINGS = 1;
    private final int maxServings;
    private final int maxSittings;

    public RecipePortions(@Nonnull RepositoryRecipePortions repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          int maxServings,
                          int maxSittings) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        domainModel = new DomainModel();

        this.maxServings = maxServings;
        this.maxSittings = maxSittings;
    }

    @Override
    protected DomainModel createDomainModelFromPersistenceModel(
            @Nonnull RecipePortionsPersistenceModel persistenceModel) {

        return new DomainModel(
                persistenceModel.getServings(),
                persistenceModel.getSittings()
        );
    }

    @Override
    protected DomainModel createDomainModelFromDefaultValues() {
        return new DomainModel(
                MIN_SERVINGS,
                MIN_SITTINGS
        );
    }

    @Override
    protected DomainModel createDomainModelFromRequestModel() {
        RecipePortionsRequest.DomainModel domainModel = ((RecipePortionsRequest) getRequest()).
                getDomainModel();

        return new DomainModel(
                domainModel.getServings(),
                domainModel.getSittings()
        );
    }

    @Override
    protected void validateDomainModelElements() {
        validateServings();
        validateSittings();
        save();
    }

    private void validateServings() {
        if (domainModel.servings < MIN_SERVINGS) {
            failReasons.add(FailReason.SERVINGS_TOO_LOW);

        } else if (domainModel.servings > maxServings) {
            failReasons.add(FailReason.SERVINGS_TOO_HIGH);
        }
    }

    private void validateSittings() {
        if (domainModel.sittings < MIN_SITTINGS) {
            failReasons.add(FailReason.SITTINGS_TOO_LOW);

        } else if (domainModel.sittings > maxSittings) {
            failReasons.add(FailReason.SITTINGS_TOO_HIGH);
        }
    }

    @Override
    protected void save() {
        if (isChanged && isDomainModelValid()) {

            useCaseDataId = idProvider.getUId();
            long currentTime = timeProvider.getCurrentTimeInMills();

            if (persistenceModel != null) {
                archiveExistingPersistenceModel(currentTime);
            }

            persistenceModel = new RecipePortionsPersistenceModel.Builder().
                    setDataId(useCaseDataId).
                    setDomainId(useCaseDomainId).
                    setServings(domainModel.servings).
                    setSittings(domainModel.sittings).
                    setCreateDate(currentTime).
                    setLastUpdate(currentTime).
                    build();

            repository.save(persistenceModel);
        }
        buildResponse();
    }

    @Override
    protected void archiveExistingPersistenceModel(long currentTime) {
        RecipePortionsPersistenceModel model = new RecipePortionsPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setLastUpdate(currentTime).
                build();

        System.out.println(TAG + "archivePersistenceModel= " + model);

        repository.save(model);
    }

    @Override
    protected void buildResponse() {
        RecipePortionsResponse response = new RecipePortionsResponse.Builder().
                setDataId(useCaseDataId).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        sendResponse(response);
    }

    private RecipePortionsResponse.Model getResponseModel() {
        return new RecipePortionsResponse.Model.Builder().
                setServings(domainModel.servings).
                setSittings(domainModel.sittings).
                setPortions(domainModel.servings * domainModel.sittings).
                build();
    }
}
