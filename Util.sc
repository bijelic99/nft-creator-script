import java.security.MessageDigest;

object ChecksumGenerator {
  private val messageDigest = MessageDigest.getInstance("MD5")

  def generateChecksum(strings: String*): String =
    messageDigest
      .digest(strings.sorted.mkString(", ").getBytes())
      .map(b => Integer.toHexString(0xff & b).toString())
      .mkString
}

object CharthesianGenerator {
  def recursiveCharthesian[T](a: Seq[Seq[T]], b: Seq[T], step: Int): Seq[Seq[T]] =
  if(step > 1) {
    val newA = for (
      seqT <- a;
      t <- b
    ) yield seqT.+:(t)
    recursiveCharthesian(newA, b, step-1)
  } else {
    a
  }
}
