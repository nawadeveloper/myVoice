package np.com.nawarajbista.myvoice

data class UserInformation(
    val fullName: String?,
    val defaultProfilePicture: String?,
    val uid: String?,
    val friendList: HashMap<String, String>,
    val post: HashMap<String, Post>
) {
    constructor(): this("", "", "", HashMap(), HashMap())
}