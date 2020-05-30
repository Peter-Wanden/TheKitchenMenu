package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public class RecipePortions extends UseCaseBase
        implements DomainDataAccess.GetDomainModelCallback<RecipePortionsPersistenceModel> {

    private static final String TAG = "tkm-" + RecipePortions.class.getSimpleName() + ": ";

    public enum FailReason implements FailReasons {
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

    @Nonnull
    private final TimeProvider timeProvider;
    @Nonnull
    private final RepositoryRecipePortions repository;
    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final List<FailReasons> failReasons;

    private String dataId = "";
    private String recipeId = "";
    private boolean isNewRequest;

    private RecipePortionsRequest.Model requestModel;
    private RecipePortionsPersistenceModel persistenceModel;

    private int accessCount;

    public RecipePortions(@Nonnull RepositoryRecipePortions repository,
                          @Nonnull UniqueIdProvider idProvider,
                          @Nonnull TimeProvider timeProvider,
                          int maxServings,
                          int maxSittings) {

        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
        this.repository = repository;
        this.maxServings = maxServings;
        this.maxSittings = maxSittings;

        requestModel = new RecipePortionsRequest.Model.Builder().getDefault().build();
        failReasons = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        accessCount ++;
        RecipePortionsRequest r = (RecipePortionsRequest) request;
        requestModel = r.getModel();

        System.out.println(TAG + "Request No:" + accessCount + " - " + r);

        if (isNewRequest(r)) {
            dataId = r.getDataId();
            recipeId = r.getDomainId();
            loadData(recipeId);
        } else {
            setupUseCase();
            processChanges();
        }
    }

    private boolean isNewRequest(RecipePortionsRequest r) {
        return isNewRequest = !r.getDomainId().equals(recipeId);
    }

    private void loadData(String recipeId) {
        repository.getActiveByDomainId(recipeId, this);
    }

    @Override
    public void onModelLoaded(RecipePortionsPersistenceModel model) {
        persistenceModel = model;
        dataId = model.getDataId();
        processChanges();
    }

    @Override
    public void onModelUnavailable() {
        persistenceModel = createNewPersistenceModel();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        processChanges();
    }

    private RecipePortionsPersistenceModel createNewPersistenceModel() {
        long currentTime = timeProvider.getCurrentTimeInMills();
        dataId = idProvider.getUId();

        return new RecipePortionsPersistenceModel.Builder().
                getDefault().
                setDataId(dataId).
                setDomainId(recipeId).
                setCreateDate(currentTime).
                setLastUpdate(currentTime).
                build();
    }

    private void setupUseCase() {
        failReasons.clear();
        isNewRequest = false;
    }

    private void processChanges() {
        validateData();
        buildResponse();
    }

    private void validateData() {
        validateServings();
        validateSittings();

        if (failReasons.isEmpty()) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private void validateServings() {
        int servings = getServings();

        if (servings < MIN_SERVINGS) {
            failReasons.add(FailReason.SERVINGS_TOO_LOW);
        } else if (servings > maxServings) {
            failReasons.add(FailReason.SERVINGS_TOO_HIGH);
        }
    }

    private int getServings() {
        return isNewRequest ? persistenceModel.getServings() : requestModel.getServings();
    }

    private void validateSittings() {
        int sittings = getSittings();

        if (sittings < MIN_SITTINGS) {
            failReasons.add(FailReason.SITTINGS_TOO_LOW);
        } else if (sittings > maxSittings) {
            failReasons.add(FailReason.SITTINGS_TOO_HIGH);
        }
    }

    private int getSittings() {
        return isNewRequest ? persistenceModel.getSittings() : requestModel.getSittings();
    }

    private void buildResponse() {
        RecipePortionsResponse.Builder builder = new RecipePortionsResponse.Builder();
        builder.setDomainId(recipeId);

        if (ComponentState.VALID_CHANGED == getComponentState()) {
            RecipePortionsPersistenceModel m = updatePersistenceModel();
            builder.setMetadata(getMetadata(m));
            persistenceModel = m;
            save();

        } else {
            builder.setMetadata(getMetadata(persistenceModel));
        }

        builder.setDomainModel(getResponseModel());
        builder.setDataId(dataId);
        sendResponse(builder.build());
    }

    private UseCaseMetadataModel getMetadata(RecipePortionsPersistenceModel m) {
        return new UseCaseMetadataModel.Builder().
                setState(getComponentState()).
                setFailReasons(new ArrayList<>(failReasons)).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(m.getCreateDate()).
                setLasUpdate(m.getLastUpdate()).
                build();
    }

    private ComponentState getComponentState() {
        boolean isValid = failReasons.contains(CommonFailReason.NONE);

        return isValid
                ?
                (isChanged() ? ComponentState.VALID_CHANGED : ComponentState.VALID_UNCHANGED)
                :
                (isChanged() ? ComponentState.INVALID_CHANGED : ComponentState.INVALID_UNCHANGED);
    }

    private boolean isChanged() {
        return !isNewRequest && (isServingsChanged() || isSittingsChanged());
    }

    private boolean isServingsChanged() {
        return persistenceModel.getServings() != requestModel.getServings();
    }

    private boolean isSittingsChanged() {
        return persistenceModel.getSittings() != requestModel.getSittings();
    }

    private RecipePortionsPersistenceModel updatePersistenceModel() {
        dataId = idProvider.getUId();
        return new RecipePortionsPersistenceModel.Builder().
                basedOnModel(persistenceModel).
                setDataId(dataId).
                setSittings(requestModel.getSittings()).
                setServings(requestModel.getServings()).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();
    }

    private RecipePortionsResponse.Model getResponseModel() {
        return new RecipePortionsResponse.Model.Builder().
                setServings(isNewRequest ?
                        persistenceModel.getServings() :
                        requestModel.getServings()).

                setSittings(isNewRequest ?
                        persistenceModel.getSittings() :
                        requestModel.getSittings()).

                setPortions(isNewRequest ?
                        persistenceModel.getServings() * persistenceModel.getSittings() :
                        requestModel.getServings() * requestModel.getSittings()).

                build();
    }

    private void sendResponse(RecipePortionsResponse r) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + r);
        if (r.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onUseCaseSuccess(r);
        } else {
            getUseCaseCallback().onUseCaseError(r);
        }
    }

    private void save() {
        repository.save(persistenceModel);
    }
}
