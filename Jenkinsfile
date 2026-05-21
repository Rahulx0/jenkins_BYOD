pipeline {
    agent any

    environment {
        RUNNER_IMAGE = 'csv302lpu/grade-runner:v1'
        REPORT_DIR = 'target/surefire-reports'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Pull Image') {
            steps {
                script {
                    echo "Pulling Docker image: ${RUNNER_IMAGE}"
                    sh "docker pull ${RUNNER_IMAGE}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "Running tests in container from image: ${RUNNER_IMAGE}"
                    sh '''
                        mkdir -p "${WORKSPACE}/${REPORT_DIR}"
                        docker run --rm \
                            -v "${WORKSPACE}:/app" \
                            -v "${WORKSPACE}/${REPORT_DIR}:/app/target/surefire-reports" \
                            -w /app \
                            ${RUNNER_IMAGE} \
                            mvn test
                    '''
                }
            }
        }

        stage('Publish Results') {
            steps {
                junit allowEmptyResults: true, testResults: "${REPORT_DIR}/*.xml"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "${REPORT_DIR}/**", allowEmptyArchive: true
        }
        success {
            echo 'Build passed successfully! All tests passed.'
        }
        failure {
            echo 'Build failed! Some tests did not pass.'
        }
    }cd /home/kratos/IdeaProjects/BYOD_test_automation

}

