package io.x10.projectexample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ProjectExampleApplication {

    static void main(String[] args) {
        SpringApplication.run(ProjectExampleApplication, args)
    }
}
