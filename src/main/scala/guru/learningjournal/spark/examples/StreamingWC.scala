package guru.learningjournal.spark.examples

import java.io.Serializable
import org.apache.spark.sql.{SparkSession, functions}
import org.apache.log4j.Logger
import org.apache.spark.sql.functions.explode



object StreamingWC extends Serializable{
  @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .master( master = "local[3]")
      .appName( name = "StreamingWC")
      .config("spark.streaming.stopGracefullyOnShutdown", value = "true")
      .config("spark.sql.shuffle.partitions", value = "3")
      .getOrCreate()


    val linesDF = spark.readStream
      .format(source = "socket")
      .option("host", "localhost")
      .option("port", "9999")
      .load()

    linesDF.printSchema()
    val wordsDF = linesDF.selectExpr("explode(split(value,' ')) as word")
    //val wordsDF = linesDF.select(explode(functions.split(linesDF("value")," ")).alias("word"))

    val countDF = wordsDF.groupBy( col1 = "word").count()

    val wordCountQuery = countDF.writeStream
      .format(source = "console")
      .option("checkpointLocation","chk-point-dir")
      .outputMode(outputMode = "complete")
      .start()

    logger.info("###################  listening to localhost : 9999 ")
    wordCountQuery.awaitTermination()


  }


}
