package np.com.nawarajbista.myvoice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Post(val status: String, val like: HashMap<String, String>, val comment: HashMap<String, CommentModel>): Parcelable {

    constructor(status: String): this(status, HashMap(), HashMap())
    constructor(): this("", HashMap(), HashMap())
}