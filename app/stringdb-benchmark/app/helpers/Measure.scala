package helpers

import org.h2.message.TraceSystem

/**
 * Created by adee on 2014.04.13..
 */
object Measure {
       def time(f: =>Any) = {
           val s = System.nanoTime
           f
           (System.nanoTime-s)/1e6
       }
}
