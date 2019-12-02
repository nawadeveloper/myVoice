package np.com.nawarajbista.myvoice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentPost(val currentPost: HashMap<String, Post>?): Parcelable {

    constructor(): this(null)
}