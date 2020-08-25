package cn.dapan.buildsrc

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        for (Map.Entry entry : project.extensions.properties.entrySet()) {
            println "properties entry key : $entry.key, value : $entry.value "
        }

        for (Map.Entry entry : project.extensions.extraProperties.properties.entrySet()) {
            println "extraProperties entry key: $entry.key, value : $entry.value"
        }
        println "plugin apply : $project.name"
        println """
                ===================================
                =                                 =
                =     Router Transform Enter      =
                =                                 =
                ===================================
                """
        def android = project.extensions.findByType(AppExtension)
        android.registerTransform(new MyTransform())
    }
}