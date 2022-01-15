pipeline {
    agent any
    
    environment
    {
        ON_SUCCESS_SEND_EMAIL=true
        ON_FAILURE_SEND_EMAIL=true
	GOOGLE_API_KEY = "AIzaSyA-DCgVotefXQ9QVG3MvMsnJvKBzNkHi50"
        DOCKER_CREDENTIAL = credentials('dockerhub-login')
    }
    
    parameters
    {
        booleanParam(name:'CLEAN_WORKSPACE',
        defaultValue:true,
        description:'Trebuie de sters mapa pentru buld-ul curent?'
        )
        booleanParam(name:'TESTING_FRONTEND',
        defaultValue:false,
        description:'Trebuie de testat frontend-ul?'
        )
    }
    
    tools {
        // Install the Maven version configured as "MAVEN11" and add it to the path.
        maven "MAVEN11"
        jdk "JAVA11"
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building the application'
                // Get some code from a GitHub repository
                git 'https://github.com/elPresedinte/Laborator3TIDPP.git'

            }
        }
        stage('Test backend')
        {
            steps{
                echo 'Testarea backend'
                bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }  
            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                   // archiveArtifacts 'target1/*.jar'
                }
            }
        }
        stage('Test front end')
        {
            when
            {
                expression
                {
                    params.TESTING_FRONTEND==true
                }
            }
            steps{
                echo "Testarea frontend ${params.TESTING_FRONTEND}"
            }    
        }
	stage('Continuous Delivery')
        {
            steps
            {
                echo "${DOCKER_CREDENTIAL_USR}"
                echo "${DOCKER_CREDENTIAL_PSW}"
                bat "docker build -t videos-service ."
                bat "docker tag videos-service:latest ionstroncea/lab4"
                bat "docker login -u ${DOCKER_CREDENTIAL_USR} -p ${DOCKER_CREDENTIAL_PSW} docker.io"
                bat "docker push ionstroncea/lab4"
                bat "docker rmi ionstroncea/lab4"
            }
        }

        stage('Continuous Deployment')
        {
            steps
            {
                echo "SSH into VBox..."
                //bat "ssh -p 3022 nick@127.0.0.1 \"cd '/home/nick/TIDPP Lab 4' && docker-compose up -d\""
            }
        }
    } 
        
    }
    post
    {
        always 
        {
            script
            {
                if(params.CLEAN_WORKSPACE==true)
                {
                    echo 'Stergerea mapei create'
                    cleanWs()
                }
                
            }
        }
        success
        {

            script
            {
                if(env.ON_SUCCESS_SEND_EMAIL)
                {
                    echo "Send email success job name: ${JOB_NAME}, build number: ${BUILD_NUMBER}, build url: ${BUILD_URL} "
                    emailext ( body: "Success! job name: ${JOB_NAME}, build number: ${BUILD_NUMBER}, build url: ${BUILD_URL}", subject: 'Build', to: 'strion.ion@gmail.com')
                    echo 'Email sent'
                }
            }
        }
        failure
        {

            script
            {
                if(env.ON_FAILURE_SEND_EMAIL)
                {
                    echo "Send email fail job name: ${JOB_NAME}, build number: ${BUILD_NUMBER}, build url: ${BUILD_URL} "
                    emailext ( body: "Fail! job name: ${JOB_NAME}, build number: ${BUILD_NUMBER}, build url: ${BUILD_URL}", subject: 'Build', to: 'strion.ion@gmail.com')
                    echo 'Email sent'
                }
            }
        }
    }
}