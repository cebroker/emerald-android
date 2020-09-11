pipeline {
  agent any
  stages {
    stage('Turn on emulator') {
        steps {
          catchError(message: "No emulator detected - It's ok", buildResult: 'SUCCESS') {
              sh "adb devices | grep emulator | cut -f1 | while read line; do adb -s $line emu kill; done"
          }
          sh "nohup /Users/cparracondor/Library/Android/sdk/emulator/emulator -avd Pixel_3a_XL_API_29 -no-snapshot -no-boot-anim -no-audio &"
          echo "Waiting for emulator"
          sh "sleep 100"
        }
    }
    stage('clean') {
        steps {
            sh './gradlew clean --stacktrace'
        }
    }
    stage('Validate') {
        steps {
            sh './gradlew compileDebugKotlin --stacktrace'
        }
    }
    stage('Emerald UI Test') {
        steps {
            catchError(message: "Build should continue if there are failed tests - It's ok", stageResult: 'UNSTABLE') {
               sh './gradlew connectedAndroidTest --stacktrace'
            }
        }
    }
    stage ('Publishing report') {
        steps {
            script {
                def PULL_REQUEST = env.CHANGE_ID

                if ( currentBuild.currentResult == "SUCCESS" ) {
                    slackSend color: '#00A950', channel: 'jenkins-android', message: ":emerald-ui: Emerald Android UI tests were successful :stableparrot: (<${REPORT_URL}/blue/organizations/jenkins/Emerald%20Android/detail/PR-${PULL_REQUEST}/${currentBuild.number}/pipeline|Open Report>)"
                }
                else if( currentBuild.currentResult == "FAILURE" ) {
                    slackSend color: '#DE350B', channel: 'jenkins-android', message: ":emerald-ui: Emerald Android UI tests failed :ahhhhhhh: (<${REPORT_URL}/blue/organizations/jenkins/Emerald%20Android/detail/PR-${PULL_REQUEST}/${currentBuild.number}/pipeline|Open Report>)"
                }
                else if( currentBuild.currentResult == "UNSTABLE" ) {
                    slackSend color: '#FFAA00', channel: 'jenkins-android', message: ":emerald-ui: Emerald Android UI tests are unstable :sleepyparrot:"
                }
                else {
                    slackSend color: "#607B7B", channel: 'jenkins-android', message: ":emerald-ui: Emerald Android UI tests are unclear :this_is_fine:"
                }

                catchError(message: "No emulator detected - It's ok", buildResult: 'SUCCESS') {
                    sh "adb devices | grep emulator | cut -f1 | while read line; do adb -s $line emu kill; done"
                }
            }
        }
    }
  }
}
