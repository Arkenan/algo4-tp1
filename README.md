# algo4-tp1

Primer trabajo práctico de la materia Algoritmos 4. El desarrollo consiste en un programa en Scala para obtener datos
de un archivo CSV, introducir los válidos en una base de datos y loguear los que no lo son.

Se utilizan las tecnologías:

- FS2 para el armado de streams.
- Doobie para los accesos a la BDD.

## Prerrequisitos

Para ejecutar el código debe tenerse instalado [SBT](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html).

## Instrucciones

### Ejecución del programa

Para ejecutar la extracción de datos debe ejecutarse `sbt` y luego, dentro de la consola:

```shell
run "<archivo.csv>"
```

Donde archivo.csv será el archivo de entrada, por ejemplo, `train.csv`, para ejecutarlo sobre 
el archivo provisto por la materia.

### Output

Luego de ejecutarse el pipeline, habrá dos efectos sobre el exterior:

- Cada fila que sea válida será insertada en la base de datos.
- El archivo `log.txt` tendrá feedback sobre las filas que no pudieron ser insertadas correctamente.

### Tests

Para ejecutar los tests unitarios debe usarse el comando:

```bash
sbt test
```

## Descripción de la solución

### Estructura del proyecto

El esquema de directorios y archivos del programa es el siguiente:

```bash
src/
- main/scala/fiuba/fp/
--- models/models.scala # Modelo en Scala de un DataSetRow
--- Run.scala # Archivo principal de ejecución, con la descripción del pipeline.
--- DB.scala # Archivo con utilidades para insertar DataSetRows a la BDD.
--- Validator.scala # Archivo con utilidades de validación.
- test/ # carpeta con los tests unitarios para la validación. 
```

### Pipeline

El trabajo se basa fundamentalmente en 4 etapas:

- Lectura de cada línea del CSV de entrada. Encoding a UTF 8.
- Validación de cada línea. El resultado es, o bien un DataSetRow válido o un Throwable.
- Inserción en la base de datos de los DatasetRow válidos. 
- Logs al archivo de salida (previo decoding desde UTF8).

Estas etapas se implementan en un pipeline de FS2. Se tiene un único `unsafeRunSync` al final para
dar la orden de consumir el pipeline.

### Manejo de errores

Las etapas de validación e inserción pueden dar errores como output. En caso de recibir un error, cada
etapa simplemente lo redireccionará a la etapa siguiente. En caso de que el input de una etapa tenga un dato válido,
se hará el procesamiento que le corresponde. Finalmente, la etapa de logs tomará los errores que hayan sido
redireccionados y los escribirá en un archivo "log.txt" para facilitar el proceso de debug.

### IO

La etapa de inserción en la base de datos tiene efectos de IO, lo cual puede observarse en que `attempt` devuelve
un `IO[Either[Throwable, Int]]`. Por este motivo, usamos `evalMap`, que evalúa los efectos de IO y cambia el tipo de
stream a `Stream[IO, Either[Throwable, Int]]`. Esto se explica en que `evalMap` es un alias para

```scala
def evalMap = flatMap(o => Stream.eval(f(o)))
```

Las únicas otras etapas donde se maneja input/output son la primera y la última, que leen o escriben un stream de 
bytes en un archivo, pero estas son manejadas con `through` y funciones provistas por `cats.io`.

### Validaciones

Las validaciones se efectuaron con la metodología "Do, don't ask". Esto significa que el validador
intentará construir un `DataSetRow` a partir de una fila, y si cualquier etapa del proceso fallara 
simplemente se devolverá un error. Por esto la salida de la etapa de validación es un 
`Either[Throwable, DatasetRow]`.

Podemos destacar los siguientes casos de causas de validación fallida:

- Filas que no tengan los suficientes campos.
- Filas que tengan vacío algún campo no opcional.
- Campos enteros o de punto flotante que no son parseables como tales.
- Campos varInt de longitud mayor a la esperada por la BDD (`curr` y `unit`).

## Schema

La base de datos tiene el siguiente esquema:

```sql
CREATE TABLE fptp.dataset
(
    id int PRIMARY KEY,
    date timestamp without time zone NOT NULL,
    open double precision,
    high double precision,
    low double precision,
    last double precision not null,
    close double precision not null,
    dif double precision not null,
    curr character varying(1) NOT NULL,
    o_vol int,
    o_dif int,
    op_vol int,
    unit character varying(4) NOT NULL,
    dollar_BN double precision NOT NULL,
    dollar_itau double precision not null,
    w_diff double precision not null,
    hash_code int not null
);
```
