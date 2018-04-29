module.exports = {
    apps: [
        {
            name: 'problems',
            args: [
                "-jar",
                "build/libs/jalgoarena-problems-2.0.0-SNAPSHOT.jar"
            ],
            script: 'java',
            env: {
                PORT: 5002,
                EUREKA_URL: 'http://localhost:5000/eureka/'
            }
        }
    ]
};
