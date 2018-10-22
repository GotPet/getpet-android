package lt.getpet.getpet.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Pets")
data class Pet(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String,
        @Embedded(prefix = "shelter")
        val shelter: Shelter,
        @ColumnInfo(name = "photo")
        val photo: String,
        @ColumnInfo(name = "short_description")
        val short_description: String,
        @ColumnInfo(name = "description")
        val description: String
) : Parcelable