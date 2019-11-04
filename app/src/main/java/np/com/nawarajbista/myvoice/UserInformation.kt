package np.com.nawarajbista.myvoice

data class UserInformation(
    val fullName: String?,
    val defaultProfilePicture: String?,
    val friendList: HashMap<String, String>,
    val post: HashMap<String, String>
) {
    constructor(): this("", "", HashMap(), HashMap())
}