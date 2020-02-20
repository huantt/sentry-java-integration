package io.x10.projectexample.worker

import groovy.transform.CompileStatic
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author huantt on 2/20/20
 */
@CompileStatic
@Component
class Tester {

    @Scheduled(fixedRate = 100000000L)
    void run(){
        throw new RuntimeException("Error example!")
    }

}
