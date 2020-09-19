package github.sachin2dehury.nitrresources.core

data class DocDetails(
    val name: String = "temp",
    val subCode: Int = 0,
    val subName: String = "temp",
    var contributor: String = "user",
    var time: Long = 0,
    var size: Double = 0.0,
    var url: String = "url",
    var type: String = "type"
)