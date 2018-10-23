package lt.getpet.getpet.data

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "PetChoices",
        foreignKeys = [ForeignKey(entity = Pet::class,
                parentColumns = ["id"],
                childColumns = ["pet_id"],
                onDelete = CASCADE)],
        indices = [
            Index(value = ["pet_id"], unique = true)
        ]
)
data class PetChoice(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Long = 0,
        @ColumnInfo(name = "pet_id")
        val petId: Long,
        @ColumnInfo(name = "is_favorite", index = true)
        val isFavorite: Boolean,
        @ColumnInfo(name = "created_at")
        var createdAt: Long = System.currentTimeMillis()
) : Parcelable