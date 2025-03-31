#!/bin/bash

# Define o endpoint base
ENDPOINT="http://localhost:4566"

# Define as filas a serem criadas
QUEUES=(
  "video-create-queue"
  "process-video-queue"
  "video-update-queue"
  "notification-push-queue"
)

# Loop para criar as filas
for QUEUE_NAME in "${QUEUES[@]}"; do
  echo "Criando a fila: $QUEUE_NAME"
  aws --endpoint="$ENDPOINT" sqs create-queue --queue-name "$QUEUE_NAME"
done

echo "Todas as filas foram criadas com sucesso!"

aws --endpoint "$ENDPOINT" s3 mb s3://videofiles

echo "Bucket criado com sucesso!"

echo $null > empty.txt
aws --endpoint-url="$ENDPOINT" s3 cp empty.txt s3://videofiles/videos/
rm -f empty.txt

echo "folders criados com sucesso!"

aws --endpoint="$ENDPOINT" dynamodb create-table \
    --table-name video_entity \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=userId,AttributeType=S \
        AttributeName=createdAt,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --global-secondary-indexes \
        "IndexName=UserCreatedAtIndex,KeySchema=[{AttributeName=userId,KeyType=HASH}, {AttributeName=createdAt,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}" \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region us-east-1


#aws --endpoint="$ENDPOINT"  dynamodb create-table \
#    --table-name video_entity \
#    --attribute-definitions \
#        AttributeName=id,AttributeType=S \
#    --key-schema \
#        AttributeName=id,KeyType=HASH \
#    --global-secondary-indexes \
#        "IndexName=idIndex,KeySchema=[{AttributeName=id,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=1,WriteCapacityUnits=1}" \
#    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
#    --region us-east-1

echo "DynamoDB criado com sucesso!"