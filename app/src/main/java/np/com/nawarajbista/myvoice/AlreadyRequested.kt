package np.com.nawarajbista.myvoice

data class AlreadyRequested(val requestSendTo: HashMap<String, String>) {
    constructor(): this(HashMap())
}