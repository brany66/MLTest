package contest

object split {
  def main(args: Array[String]): Unit = {
    val str = "A$B$#"


    val sp1 = str.split('$')

     val sp2 = str.split("$")

    println(sp1.length + " " + sp2.length)
  }
}
