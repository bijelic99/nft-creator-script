import $ivy.`com.typesafe:config:1.4.2`
import com.typesafe.config.ConfigFactory
import java.io.File


private val configuration = ConfigFactory.parseFile(new File("./resources/script.conf"))

val minAttributes = configuration.getInt("minAttributes")
val maxAttributes = configuration.getInt("maxAttributes")