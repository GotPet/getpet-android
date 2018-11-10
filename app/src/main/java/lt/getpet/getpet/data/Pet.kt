package lt.getpet.getpet.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Pets")
@JsonClass(generateAdapter = true)
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
        @Json(name = "short_description")
        val shortDescription: String,
        @ColumnInfo(name = "description")
        val description: String
) : Parcelable