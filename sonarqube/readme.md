## PRO 

helm repo add sonarqube https://SonarSource.github.io/helm-chart-sonarqube
helm repo update
helm upgrade --install -n sonarqube sonarqube-lts sonarqube/sonarqube-lts --create-namespace -f values.yaml
## DEV 

helm repo add sonarqube https://SonarSource.github.io/helm-chart-sonarqube
helm repo update
helm upgrade --install -n sonarqube-dev sonarqube-lts-dev sonarqube/sonarqube-lts --create-namespace -f values-dev.yaml

