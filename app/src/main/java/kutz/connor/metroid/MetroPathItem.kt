package kutz.connor.metroid

data class MetroPathItem(var distanceToPrev : Int){
    var lineCode : String = ""
    var seqNum : Int = 0
    var stationCode : String = ""
    var stationName : String = ""
}