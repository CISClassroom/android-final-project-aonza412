package th.ac.kku.cis.mobileapp.myapplication

class user {
    companion object Factory {
        fun create(): user = user()
    }
    var id: String? = null
    var name: String? = null
    var objectId: String? = null
    var pass: String? = null
}
class user_point {
    companion object Factory {
        fun create(): user_point = user_point()
    }
    var id: String? = null
    var date_time: String? = null
    var lon: String? = null
    var lat: String? = null
    var objectId: String? = null
}