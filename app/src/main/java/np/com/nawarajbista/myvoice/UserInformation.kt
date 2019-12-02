package np.com.nawarajbista.myvoice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInformation(
    val fullName: String?,
    val defaultProfilePicture: String?,
    val uid: String?,
    val friendList: HashMap<String, String>,
    val post: HashMap<String, Post>
): Parcelable {
    constructor(): this("", "", "", HashMap(), HashMap())
}