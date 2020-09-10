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
    stage('Unit Test Wallet') {
        steps {
            catchError(message: "Build should continue if there are failed tests - It's ok", stageResult: 'UNSTABLE') {
               sh './gradlew connectedAndroidTest --stacktrace'
            }
        }
    }
    stage ('Publishing report') {
        steps {
            script {
                def now = new Date()
                sh "echo ${now} | cat - wallet/build/reports/gordonDemo/test-report.html > temp && mv temp wallet/build/reports/gordonDemo/test-report.html"
            }

            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: "wallet/build/reports/gordonDemo", reportFiles: "test-report.html", reportName: 'UI test report', reportTitles: 'Tests Results'
                ])
            if ( currentBuild.currentResult == "SUCCESS" ) {
                slackSend color: '#00A950', channel: 'jenkins-android', message: "Custom Components UI ${branchName} ${size} tests were successful :celebrate: (<${REPORT_URL}/job/Wallet%20UI%20Test%20Specific%20Branch/${currentBuild.number}/UI_20test_20report/|Open Report>)"
            }
            else if( currentBuild.currentResult == "FAILURE" ) {
                slackSend color: '#DE350B', channel: 'jenkins-android', message: "Custom Components UI ${branchName} ${size} tests failed :ahhhhhhh: (<${REPORT_URL}/job/Wallet%20UI%20Test%20Specific%20Branch/${currentBuild.number}/UI_20test_20report/|Open Report>)"
            }
            else if( currentBuild.currentResult == "UNSTABLE" ) {
                slackSend color: '#FFAA00', channel: 'jenkins-android', message: "Custom Components UI ${branchName} ${size} tests are unstable :manzanillo-high:"
            }
            else {
                slackSend color: "#607B7B", channel: 'jenkins-android', message: "Custom Components UI ${branchName} ${size} tests are unclear :mag:"
            }

            catchError(message: "No emulator detected - It's ok", buildResult: 'SUCCESS') {
                sh "adb devices | grep emulator | cut -f1 | while read line; do adb -s $line emu kill; done"
            }
        }
    }
  }
}
