package models



import play.api.libs.json.{JsObject, JsValue, JsArray, Json}
import scala.collection.mutable.HashMap
import helpers.Measure
import play.api.db.DB
import anorm._
import play.api.libs.json.JsObject
import play.api.Play.current
import org.anormcypher.Cypher

/**
 * Created by adee on 2014.05.11..
 */


object Benchmarks{
  val benchmarks = new HashMap[String,Benchmark]
  def getAll = {
    var arr:JsArray = new JsArray()
    this.benchmarks.foreach((e: (String, Benchmark)) =>{
       arr =  arr.append(e._2.runBench)

    } )
    arr
  }
  def runBench(name:String)={
    val bench = this.benchmarks.get(name).get
    Json.arr(bench.runBench)
  }

  def put(bench:Benchmark)={
     this.benchmarks.put(bench.getClass.getName,bench)
  }
  put(CountProteinMysql)
  put(OrderByCombinedScoreMysql)
  put(MaxInteractionMysql)
  put(MinInteractionMysql)
  put(CountProteinNeo)
  put(OrderByCombinedScoreNeo)
  put(MaxInteractionNeo)
  put(MinInteractionNeo)

}


trait Benchmark {
  def createResult(description:String, label:String, value:Double):JsObject = {
    Json.obj(
      "description"->description,
      "label" -> label,
      "value" -> value,
      "key" ->this.getClass.getName,
      "time" -> System.nanoTime
    )
  }
  def runBench:JsObject
}


object CountProteinMysql extends Benchmark {
   val bench = "SELECT count(i2.protein2) FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1"
   def runBench = {
     val time =  Measure.time({
       DB.withConnection {
         implicit conn =>
           SQL(bench).execute()
       }
     })
     this.createResult(bench,"Count all protein interactions",time)
   }

}

object OrderByCombinedScoreMysql extends Benchmark {
  val bench = "SELECT count(i2.protein2) FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1 order by i1.combined_score"
  def runBench = {
    val time =  Measure.time({
      DB.withConnection {
        implicit conn =>
          SQL(bench).execute()
      }
    })
    this.createResult(bench,"order by combined_score",time)
  }

}


object MaxInteractionMysql extends Benchmark {
  val bench ="""
     SELECT max(cnt)  FROM  (SELECT count(i2.protein2) as cnt FROM interactions AS i1  INNER JOIN interactions AS i2 ON
      i1.protein2 = i2.protein1 GROUP BY  i1.protein1 ) as counts
    """.stripMargin
  def runBench = {
    val time =  Measure.time({
      DB.withConnection {
        implicit conn =>
          SQL(bench).execute()
      }
    })
    this.createResult(bench,"maximum interaction on protein",time)
  }

}


object MinInteractionMysql extends Benchmark {
  val bench ="""
     SELECT min(cnt)  FROM  (SELECT count(i2.protein2) as cnt FROM interactions AS i1  INNER JOIN interactions AS i2 ON
      i1.protein2 = i2.protein1 GROUP BY  i1.protein1 ) as counts
             """.stripMargin
  def runBench = {
    val time =  Measure.time({
      DB.withConnection {
        implicit conn =>
          SQL(bench).execute()
      }
    })
    this.createResult(bench,"minimum interaction on protein",time)
  }

}




object CountProteinNeo extends Benchmark {
  val bench ="""
     MATCH (a)-[:`REL`]->(b) RETURN count(a)
             """.stripMargin
  def runBench = {
    val time =  Measure.time({
      val req = Cypher(bench)
      req()
    })
    this.createResult(bench,"count all interaction neo",time)
  }

}


object OrderByCombinedScoreNeo extends Benchmark {
  val bench ="""
     MATCH (a)-[:`REL`]->(b) RETURN a,b  ORDER BY a.combined_score
             """.stripMargin
  def runBench = {
    val time =  Measure.time({
      val req = Cypher(bench)
      req()
    })
    this.createResult(bench,"order by combined score neo",time)
  }

}




object MaxInteractionNeo extends Benchmark {
  val bench ="""
     MATCH p = (a)-[r:`REL`]->(b) RETURN max( reduce(n = 0, c IN relationships(p) | n+1  )) as reduction
             """.stripMargin
  def runBench = {
    val time =  Measure.time({
      val req = Cypher(bench)
      req()
    })
    this.createResult(bench,"max interactions neo",time)
  }

}



object MinInteractionNeo extends Benchmark {
  val bench ="""
     MATCH p = (a)-[r:`REL`]->(b) RETURN min( reduce(n = 0, c IN relationships(p) | n+1  )) as reduction
             """.stripMargin
  def runBench = {
    val time =  Measure.time({
      val req = Cypher(bench)
      req()
    })
    this.createResult(bench,"min interactions neo",time)
  }

}