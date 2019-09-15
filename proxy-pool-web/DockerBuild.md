-- 构建启动容器
docker-compose -f docker-compose.yml up -d
-- 删除容器,镜像
docker-compose -f docker-compose.yml down --rmi all
