pipeline {
    agent any

    environment {
        // Use minimal Alpine and install Java+Maven at runtime
        RUNNER_IMAGE = 'alpine:latest'
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
                    echo "Running tests in container from image: ${RUNNER_IMAGE} (will install OpenJDK and Maven)"
                    sh '''
                        mkdir -p "${WORKSPACE}/${REPORT_DIR}"
                        docker run --rm \
                            -v "${WORKSPACE}:/app" \
                            -v "${WORKSPACE}/${REPORT_DIR}:/app/target/surefire-reports" \
                            -w /app \
                            ${RUNNER_IMAGE} \
                            sh -c "apk add --no-cache openjdk17-jdk maven && mvn -B test"
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
    }
}

