package com.github.gaofeicm.dddsupportpluginintellij.services

import com.intellij.openapi.project.Project
import com.github.gaofeicm.dddsupportpluginintellij.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
