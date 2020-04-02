package th.ac.kku.cis.mobileapp.myapplication

class event {
    companion object Factory {
        fun create(): event = event()
    }
    var user_id: String? = null
    var event_name: String? = null
    var objectId: String? = null
    var done: Boolean? = false
}
class event_data {
    companion object Factory {
        fun create(): event_data = event_data()
    }
    var event_name: String? = null
    var event_detail: String? = null
    var event_unit: String? = null
    var event_side: String? = null
    var objectId: String? = null
    var date_start: String? = null
    var date_end: String? = null
}
class add_event {
    companion object Factory {
        fun create(): add_event = add_event()
    }
    var name: String? = null
    var id: String? = null
    var objectId: String? = null
    var done: Boolean? = false
}
