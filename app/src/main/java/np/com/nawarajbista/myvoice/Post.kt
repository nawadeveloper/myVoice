package np.com.nawarajbista.myvoice

class Post(val status: String, val like: HashMap<String, String>, val comment: HashMap<String, String>) {

    constructor(status: String): this(status, HashMap(), HashMap())
    constructor(): this("", HashMap(), HashMap())
}