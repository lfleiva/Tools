SELECT * FROM tools.migracion;


SELECT E.codigo AS 'ID ESTRATEGIA', E.nombre AS 'ESTRATEGIA', IF (E.multientidad = 0, 'FALSE', 'TRUE') AS 'MULTIENTIDAD', T.entidad AS 'ENTIDAD', M.cq_2016 AS 'CQ 2016', M.cq_2017 AS 'CQ 2017', 
M.fecha_ultimo_cambio AS 'FECHA ULTIMO CAMBIO', M.frecuencia AS 'FRECUENCIA', E.complejidad AS 'COMPLEJIDAD', M.transacciones AS 'TRANSACCIONES', 
M.frecuencia_peso AS 'PESO FRECUENCIA', M.complejidad_peso AS 'PESO COMPLEJIDAD', M.transacciones_peso AS 'PESO TRANSACCIONES', M.peso AS 'PESO'
FROM tools.migracion M
INNER JOIN tools.estrategia E ON M.id_estrategia = E.id
INNER JOIN tools.transacciones T ON E.id = T.id_estrategia