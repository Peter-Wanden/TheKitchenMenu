package com.example.peter.thekitchenmenu.data.entity;

import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.FtsOptions;

import static com.example.peter.thekitchenmenu.data.entity.FtsProdComm.TABLE_FTS_PROD_COMM;

@Entity(tableName = TABLE_FTS_PROD_COMM)
@Fts4(contentEntity = DmProdComm.class,
        tokenizer = FtsOptions.Tokenizer.UNICODE61,
        prefix = {2,3,4})
public class FtsProdComm {

    public static final String TAG = FtsProdComm.class.getSimpleName();
    public static final String TABLE_FTS_PROD_COMM = "fts_prod_comm";

    private String description;
    private String madeBy;

    public FtsProdComm(String description, String madeBy) {
        this.description = description;
        this.madeBy = madeBy;
    }

    public String getDescription() {
        return description;
    }

    public String getMadeBy() {
        return madeBy;
    }
}
