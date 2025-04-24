
# Fullstack Developer Assignment - Application Module

Bu proje, Repsy için bir Fullstack Developer Assignment çözümünün **uygulama kısmını** içerir. Spring Boot ile geliştirilen uygulama, PostgreSQL ve MinIO kullanılarak yapılandırılmıştır ve tüm sistem Docker ile container bazlı çalıştırılabilir.

---

## 🚀 Proje Yapısı

```bash
fullstackdevassignment-app/
├── Dockerfile                    # Spring Boot uygulaması için Docker imajı
├── docker-compose.yaml          # PostgreSQL, MinIO ve uygulamayı birlikte çalıştırır
├── settings.xml                 # Maven için Repsy repo erişim ayarı (Docker'da kullanılmak üzere)
├── src/                         # Uygulama kaynak kodu
└── pom.xml                      # Maven konfigürasyonu
```

---

## ⚙️ Teknolojiler

- Java 17
- Spring Boot 3.4.4
- PostgreSQL 16
- MinIO (S3 uyumlu object storage)
- Maven
- Docker & Docker Compose

---

## 📦 Storage Entegrasyonu

Uygulama, storage işlemleri için harici kütüphaneler (`storage-common`, `storage-filesystem`, `storage-object`) kullanır. Bu kütüphaneler **Repsy Maven repo'ya deploy edilmiştir** ve `pom.xml` dosyasında tanımlı olarak gelir.

---

## ⚙️ Konfigürasyon

### application.properties:

```properties
storage.strategy=object-storage

minio.endpoint=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin
minio.bucket-name=packages

spring.datasource.url=jdbc:postgresql://localhost:5432/repsy_repository
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

---

## 🐳 Docker ile Çalıştırma

### 1. Build ve Çalıştır:
```bash
docker-compose up --build
```

### 2. Servisler:
- **http://localhost:8080** → Uygulama API
- **http://localhost:9001** → MinIO Panel (kullanıcı: `minioadmin`)
- **PostgreSQL:** `localhost:5433` (kullanıcı: `postgres`, şifre: `password`)
---

## 📦 Docker Image (Repsy Container Registry)

Bu uygulamanın Docker imajı, Repsy Container Registry üzerinde yayınlanmıştır:

```bash
docker pull repo.repsy.io/burakkoc/fullstackdevassignment/fullstackapp:latest
```

## 📚 API Belgeleri

Tüm endpoint'ler Swagger/OpenAPI arayüzü ile belgelenmiştir:

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Repsy Entegrasyonu

`.m2/settings.xml` içinde şu yapı bulunmalıdır:

```xml
<settings>
  <servers>
    <server>
      <id>repsy</id>
      <username>burakkoc</username>
      <password>GİZLİ_ANAHTAR</password>
    </server>
  </servers>
</settings>
```

Bu dosya, Docker build sırasında `settings.xml` olarak container içine kopyalanır.

---

## ✍️ Yazar

**Burak Koç**  
👨‍💻 Junior Fullstack Developer Adayı  
📧 kocburak2002@gmail.com
