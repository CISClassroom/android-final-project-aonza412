package th.ac.kku.cis.mobileapp.myapplication

interface ItemRowListener {
//    fun onItemDelete(itemID: String,index: Int)
    fun modifyItemState(itemID: String,index: Int,status: Boolean)
}