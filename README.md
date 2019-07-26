## docker打包
```bash

rm -rf ~/.m2/repository/io/github/harvies/
 
sudo mvn  clean install dockerfile:build -DskipTests -Ptest
```