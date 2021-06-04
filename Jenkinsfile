pipeline {
  agent { docker { image 'gradle:jdk16-hotspot' } }
  stages {
    stage('log version info') {
      steps {
        sh 'gradle clean build'
      }
    }
  }
}