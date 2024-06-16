
use Facturamysql;
CREATE TABLE CLIENTES (
   CLICODIGO CHAR(7) PRIMARY KEY, 
   CLINOMBRE1 CHAR(60) NOT NULL, 
   CLINOMBRE2 CHAR(60),
   CLIAPELLIDO1 CHAR(60) NOT NULL, 
   CLIAPELLIDO2 CHAR(60), 
   CLINOMBRE CHAR(121) NOT NULL, 
   CLIIDENTIFICACION CHAR(10) NOT NULL UNIQUE,
   CLIDIRECCION CHAR(60) NOT NULL,
   CLITELEFONO CHAR(10) NOT NULL,
   CLICELULAR CHAR(10) NOT NULL,
   CLIEMAIL CHAR(60), 
   CLITIPO CHAR(3) NOT NULL,
   CLISTATUS CHAR(3) NOT NULL, 
   CLIIMAGEN LONGBLOB,
   CONSTRAINT CK_CLIAPELLIDO1 CHECK (CLIAPELLIDO1 REGEXP '^[A-Za-zÁÉÍÓÚáéíóúÑñ. ]+$'),
   CONSTRAINT CK_CLIAPELLIDO2 CHECK (CLIAPELLIDO2 IS NULL OR CLIAPELLIDO2 REGEXP '^[A-Za-zÁÉÍÓÚáéíóúÑñ. ]+$'),
   CONSTRAINT CK_CLINOMBRE1 CHECK (CLINOMBRE1 REGEXP '^[A-Za-zÁÉÍÓÚáéíóúÑñ. ]+$'),
   CONSTRAINT CK_CLINOMBRE2 CHECK (CLINOMBRE2 IS NULL OR CLINOMBRE2 REGEXP '^[A-Za-zÁÉÍÓÚáéíóúÑñ. ]+$'),
   CONSTRAINT CK_CLITELEFONO CHECK (CHAR_LENGTH(CLITELEFONO) = 10 AND CLITELEFONO REGEXP '^[0-9]+$'), 
   CONSTRAINT CK_CLICELULAR CHECK (CHAR_LENGTH(CLICELULAR) = 10 AND CLICELULAR REGEXP '^[0-9]+$'), 
   CONSTRAINT CK_CLIEMAIL CHECK (CLIEMAIL IS NULL OR (CHAR_LENGTH(CLIEMAIL) <= 60 AND CLIEMAIL REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')), 
   CONSTRAINT CK_CLITIPO CHECK (CLITIPO IN ('VIP', 'NOR', 'EMP')), 
   CONSTRAINT CK_CLISTATUS CHECK (CLISTATUS IN ('ACT', 'INA')),
   CONSTRAINT CK_CLIDIRECCION_FORMAT CHECK (CLIDIRECCION REGEXP '^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ ,. ]+$')
);

-- Inserción de registros corregidos
INSERT INTO CLIENTES (CLICODIGO, CLINOMBRE1, CLINOMBRE2, CLIAPELLIDO1, CLIAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS)
VALUES 
('CLI001', 'Javier', NULL, 'Cóndor', NULL, 'Javier Cóndor', '0702996502', 'Av 9 Octubre 729 y Boyaca', '0229809800', '0992996500', 'ventas@elrosado.com.ec', 'NOR', 'ACT');

INSERT INTO CLIENTES (CLICODIGO, CLINOMBRE1, CLINOMBRE2, CLIAPELLIDO1, CLIAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS)
VALUES 
('CLI002', 'Pamela', NULL, 'Cortez', NULL, 'Pamela Cortez', '0602996504', 'Km 16 via Daule Pascuales', '0460052380', '0992996500', 'ventas@ecuacorriente.ec', 'NOR', 'ACT');

INSERT INTO CLIENTES (CLICODIGO, CLINOMBRE1, CLINOMBRE2, CLIAPELLIDO1, CLIAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS)
VALUES 
('CLI003', 'Veronica', 'Antonela', 'Fritz', NULL, 'Veronica Antonela Fritz', '2102996506', 'Cdla Sta Leonor Mz 6 solar 17', '0450040400', '0992996500', 'ventas@difare.com.ec', 'NOR', 'ACT');

CREATE TABLE UNIDADES_MEDIDA (
   UMCODIGO             CHAR(3) NOT NULL,
   UMDESCRIPCION        VARCHAR(100) NOT NULL,
   PRIMARY KEY (UMCODIGO)
);

/* Insertamos ejemplos de unidades de medida */
INSERT INTO UNIDADES_MEDIDA (UMCODIGO, UMDESCRIPCION) VALUES
('kg', 'Kilogramos'),    /* Para peso */
('gr', 'Gramos'),        /* Para peso */
('lt', 'Litros'),        /* Para volumen líquido */
('ml', 'Mililitros'),    /* Para volumen líquido */
('un', 'Unidades'),      /* Para conteo de artículos */
('m', 'Metros'),         /* Para longitud */
('cm', 'Centímetros'),   /* Para longitud */
('mm', 'Milímetros'),    /* Para longitud */
('qq', 'Quintal');

/* Creación de tabla para estados del producto */
CREATE TABLE ESTADOS_PRODUCTO (
   ESTCODIGO            CHAR(3) NOT NULL,
   ESTDESCRIPCION       VARCHAR(100) NOT NULL,
   PRIMARY KEY (ESTCODIGO)
);

/* Insertamos algunos estados posibles para los productos */
INSERT INTO ESTADOS_PRODUCTO (ESTCODIGO, ESTDESCRIPCION) VALUES
('ACT', 'Activo'),       /* Disponible para la venta o uso */
('INA', 'Inactivo'),     /* No disponible temporalmente */
('DIS', 'Discontinuado'),/* No volverá a estar disponible */
('ESC', 'Escasez');      /* Poca cantidad disponible */

/* Creación de la tabla PRODUCTOS */
CREATE TABLE PRODUCTOS (
   PROCODIGO            VARCHAR(10) NOT NULL,
   PRODESCRIPCION       VARCHAR(100) NOT NULL,
   PROUNIDADMEDIDA      CHAR(3) NOT NULL,
   PROSALDOINICIAL      DECIMAL(9,2) NOT NULL CHECK (PROSALDOINICIAL >= 0),
   PROINGRESOS          DECIMAL(9,2) NOT NULL CHECK (PROINGRESOS >= 0),
   PROEGRESOS           DECIMAL(9,2) NOT NULL CHECK (PROEGRESOS >= 0),
   PROAJUSTES           DECIMAL(9,2) NOT NULL CHECK (PROAJUSTES >= 0),
   PROSALDOFINAL        DECIMAL(9,2) NOT NULL CHECK (PROSALDOFINAL >= 0),
   PROCOSTOUM           DECIMAL(7,2) NOT NULL CHECK (PROCOSTOUM >= 0),
   PROPRECIOUM          DECIMAL(7,2) NOT NULL CHECK (PROPRECIOUM >= 0),
   PROSTATUS            CHAR(3) NOT NULL,
   PROFOTO              LONGBLOB,
   PRIMARY KEY (PROCODIGO),
   CONSTRAINT fk_unidad_medida FOREIGN KEY (PROUNIDADMEDIDA) REFERENCES UNIDADES_MEDIDA(UMCODIGO),
   CONSTRAINT fk_prostatus FOREIGN KEY (PROSTATUS) REFERENCES ESTADOS_PRODUCTO(ESTCODIGO)
);

INSERT INTO PRODUCTOS (
    PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS,
    PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROPRECIOUM, PROSTATUS
) VALUES
    ('P-0001', 'CEREAL TRIGO ENTERO', 'un', 100, 50, 20, 5, 135, 0.50, 1.00, 'ACT'),
    ('P-0002', 'MORA FRUTO COMPLETO', 'un', 200, 100, 50, 10, 260, 0.20, 0.45, 'ACT'),
    ('P-0003', 'CARNE DE CERDO CON HUESO', 'kg', 150, 70, 30, 0, 190, 3.50, 5.00, 'ACT'),
    ('P-0004', 'SARDINAS EN CONSERVA', 'un', 300, 150, 75, 25, 400, 0.85, 1.50, 'ACT'),
    ('P-0005', 'LECHE LÍQUIDA PASTEURIZADA', 'lt', 500, 300, 200, 0, 600, 0.90, 1.20, 'ACT'),
    ('P-0006', 'ATÚN EN CONSERVA', 'un', 400, 200, 100, 50, 550, 0.95, 1.75, 'ACT'),
    ('P-0007', 'JUGO DE NARANJA', 'lt', 250, 120, 80, 0, 290, 0.55, 1.10, 'ACT'),
    ('P-0008', 'HARINA DE TRIGO', 'kg', 800, 400, 300, 50, 950, 0.20, 0.40, 'ACT'),
    ('P-0009', 'ARROZ BLANCO', 'un', 600, 300, 150, 30, 780, 0.15, 0.30, 'ACT'),
    ('P-0010', 'FRIJOLES NEGROS', 'kg', 500, 250, 125, 25, 650, 0.45, 0.90, 'ACT');
 
CREATE TABLE FACTURAS (
   FACNUMERO            CHAR(9)              NOT NULL,
   CLICODIGO            CHAR(7)              NOT NULL,
   FACFECHA             DATE                 NOT NULL,
   FACSUBTOTAL          NUMERIC(9,2)         NOT NULL,
   FACDESCUENTO         NUMERIC(9,2)         NULL,
   FACIVA               NUMERIC(9,2)         NULL,
   FACICE               NUMERIC(9,2)         NULL,
   FACFORMAPAGO         CHAR(5)              NOT NULL,
   FACSTATUS            CHAR(3)              NOT NULL,
   CONSTRAINT PK_FACTURAS PRIMARY KEY (FACNUMERO),
   CONSTRAINT FK_FACTURAS_CLICODIGO FOREIGN KEY (CLICODIGO) REFERENCES CLIENTES (CLICODIGO),
   CONSTRAINT CK_FACSUBTOTAL CHECK (FACSUBTOTAL >= 0),
   CONSTRAINT CK_FACDESCUENTO CHECK (FACDESCUENTO IS NULL OR FACDESCUENTO >= 0),
   CONSTRAINT CK_FACIVA CHECK (FACIVA IS NULL OR FACIVA >= 0),
   CONSTRAINT CK_FACICE CHECK (FACICE IS NULL OR FACICE >= 0),
   CONSTRAINT CK_FACFORMAPAGO CHECK (FACFORMAPAGO IN ('EFECT', 'TARCR', 'TARDB', 'TRANS', 'CHEQ')),
   CONSTRAINT CK_FACSTATUS CHECK (FACSTATUS IN ('PEN', 'PAG', 'CANC'))
);

CREATE TABLE PXF (
   FACNUMERO            CHAR(9)              NOT NULL,
   PROCODIGO            CHAR(9)              NOT NULL,
   PXFCANTIDAD          NUMERIC(9,2)         NOT NULL,
   PXFVALOR             NUMERIC(7,2)         NOT NULL,
   PXFSUBTOTAL          NUMERIC(9,2)         NOT NULL,
   PXFSTATUS            CHAR(3)              NOT NULL,
   CONSTRAINT PK_PXF PRIMARY KEY (FACNUMERO, PROCODIGO),
   CONSTRAINT FK_PXF_FACNUMERO FOREIGN KEY (FACNUMERO) REFERENCES FACTURAS (FACNUMERO),
   CONSTRAINT FK_PXF_PROCODIGO FOREIGN KEY (PROCODIGO) REFERENCES PRODUCTOS (PROCODIGO),
   CONSTRAINT CK_PXFCANTIDAD CHECK (PXFCANTIDAD > 0),
   CONSTRAINT CK_PXFVALOR CHECK (PXFVALOR > 0),
   CONSTRAINT CK_PXFSUBTOTAL CHECK (PXFSUBTOTAL = PXFCANTIDAD * PXFVALOR),
   CONSTRAINT CK_PXFSTATUS CHECK (PXFSTATUS IN ('ACT', 'INA'))
);

select * from CLIENTES;
select * from Facturas; 	

DELETE FROM facturas WHERE FACNUMERO = 'FAC-003';
DELETE FROM facturas WHERE FACNUMERO = 'FAC-002';

DELETE FROM PXF WHERE FACNUMERO = 'FAC-003';
DELETE FROM PXF WHERE FACNUMERO = 'FAC-002';

-- Eliminar registros de clientes específicos
DELETE FROM CLIENTES WHERE CLIIDENTIFICACION = '1726442120';

SELECT * FROM PRODUCTOS;
SELECT * FROM CLIENTES;
select * from facturas;
select * from pxf;

-- Insertar la factura en FACTURAS
INSERT INTO FACTURAS (FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACDESCUENTO, FACIVA, FACICE, FACFORMAPAGO, FACSTATUS)
VALUES 
('FAC-001', 'CLI002', '2024-06-15', 200.00, 0.00, 20.00, 0.00, 'TARCR', 'PEN');

-- Insertar detalle de productos en PXF
INSERT INTO PXF (FACNUMERO, PROCODIGO, PXFCANTIDAD, PXFVALOR, PXFSUBTOTAL, PXFSTATUS)
VALUES 
('FAC-001', 'P-0002', 2, 0.45, 0.90, 'ACT'),  -- Ejemplo de producto 1
('FAC-001', 'P-0004', 1, 1.50, 1.50, 'ACT');  -- Ejemplo de producto 2
DELETE FROM FACTURAS
WHERE FACNUMERO = 'FAC-0002';