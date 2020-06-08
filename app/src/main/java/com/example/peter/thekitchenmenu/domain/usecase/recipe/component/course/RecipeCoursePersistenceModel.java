package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

public final class RecipeCoursePersistenceModel
        extends
        BaseDomainPersistenceModel {

    @Nonnull
    private Set<RecipeCoursePersistenceModelItem> persistenceModelItems;

    private RecipeCoursePersistenceModel(){
        persistenceModelItems = new HashSet<>();
    }

    @Nonnull
    public Set<RecipeCoursePersistenceModelItem> getPersistenceModelItems() {
        return persistenceModelItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeCoursePersistenceModel)) return false;
        if (!super.equals(o)) return false;
        RecipeCoursePersistenceModel model = (RecipeCoursePersistenceModel) o;
        return persistenceModelItems.equals(model.persistenceModelItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), persistenceModelItems);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCoursePersistenceModel{" +
                "persistenceModelItems=" + persistenceModelItems +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeCoursePersistenceModel> {

        public Builder() {
            domainModel = new RecipeCoursePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.persistenceModelItems = new HashSet<>();
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        public Builder baseOnModel(RecipeCoursePersistenceModel persistenceModel) {
            domainModel.dataId = persistenceModel.getDataId();
            domainModel.domainId = persistenceModel.getDomainId();
            domainModel.persistenceModelItems = persistenceModel.getPersistenceModelItems();
            domainModel.createDate = persistenceModel.getCreateDate();
            domainModel.lastUpdate = persistenceModel.getLastUpdate();
            return self();
        }

        public Builder setPersistenceModelItems(Set<RecipeCoursePersistenceModelItem> items) {
            domainModel.persistenceModelItems = items;
            return self();
        }
    }
}
