package np.com.nawarajbista.myvoice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JustUserInformation(
    val fullName: String?,
    val defaultProfilePicture: String?,
    val uid: String?
): Parcelable {
    constructor(): this("", "", "")
}