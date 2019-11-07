package np.com.nawarajbista.myvoice

class Post(val status: String, val like: Int, val comment: HashMap<String, String>) {

    constructor(status: String): this(status,0, HashMap())
    constructor(): this("", 0, HashMap())
}