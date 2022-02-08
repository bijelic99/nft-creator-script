import java.security.MessageDigest;

object ChecksumGenerator {
  private val messageDigest = MessageDigest.getInstance("MD5")

  def generateChecksum(strings: String*): String =
    messageDigest
      .digest(strings.sorted.mkString(", ").getBytes())
      .map(b => Integer.toHexString(0xff & b).toString())
      .mkString
}
