#!/bin/bash

YELLOW='\e[93m'
DEFAULT='\e[39m'

SERVICE_NAME="jwebapp"

# env: `test` or `prod`
ENV=$1
if [ -z "$ENV" ]
then
      echo -e "ENV parameter is empty. Provide '${YELLOW}test${DEFAULT}' or '${YELLOW}prod${DEFAULT}'"
      exit 1;
fi

GIT_HASH=$(git rev-parse --short HEAD)
GIT_BRANCH=$(git branch --show-current)
GIT_REPO_NAME=$(basename `git rev-parse --show-toplevel`)
echo -e "Git: ${GIT_BRANCH} (${GIT_HASH})"

echo -e "Environment '${YELLOW}${ENV}${DEFAULT}' is going to be deployed to AWS"

terraform -version

terraform init \
  -backend-config="key=${SERVICE_NAME}.${ENV}.tfstate"

#terraform plan

terraform apply -var-file="env/${ENV}.tfvars" -auto-approve \
  -var="service_name=${SERVICE_NAME}" \
  -var="environment_name=${ENV}" \
  -var="git_branch=${GIT_HASH}" \
  -var="git_hash=${GIT_BRANCH}" \
  -var="git_repo_name=${GIT_REPO_NAME}"
