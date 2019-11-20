package np.com.nawarajbista.myvoice


data class UserDataFireBase(val fullName: String?, val email: String?, val defaultProfilePicture: String?, val uid: String?) {
    constructor(): this("","", "", "")
}