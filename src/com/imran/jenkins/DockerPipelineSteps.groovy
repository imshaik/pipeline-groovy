package com.imran.jenkins;

class DockerPipelineSteps implements Serializable {
 
 static final def docker_opts   = "-v /home/root/.m2:/lowes/workspace/"

  def steps

  DockerPipelineSteps(steps) { this.steps = steps }

  def mavenbuild(mavenimage,goals) {
     try {
       mavenimage.inside(docker_opts) { c ->
       steps.sh "mvn /lowes/workspace ${goals}"
        }
      } finally { 
       }
   }


  def RemoveNoneImage() {
	def NoneImageid = steps.sh (
	script: "docker images | grep '<none>' | tr -s ' ' | cut -d ' ' -f3",
	returnStdout: true
        ).trim()
    def command = "docker rmi -f ${NoneImageid}"
      steps.sh "${command}"
    }

  def RemoveImage(dockerreponame, buildlabel) {
	def Imageid = "docker rmi -f shaikimranashrafi/${dockerreponame}:${buildlabel}"
	steps.sh "${Imageid}"
	}

  def RemoveContainer() {
	def ContainerId = "docker ps -a -q | xargs -n 1 -I {} sudo docker rm -f {}"
        steps.sh "${ContainerId}"
    }
  }

