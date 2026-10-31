# Proceso
2. Proceso para generar distintos tipos "simples" de probabilidades a calcular y las combinaciones entre esos tipos.
   También calcula las probabilidades asociadas a cada tipo, ya que éstas dependen de la definición del tipo.<br>
   `draw-define-types-and-combinations`<br>
   El propio micro define los diferentes tipos a generar. Los tipos y combinaciones se crearán por defecto activados<br>

## Definición
- El micro define una lista de tipos de probabilidades a generar. Los crea como activos, si no existen ya.<br>
- Calcula las probabilidades por sorteo para aquellos tipos activos.<br>
- Por último, genera las combinaciones entre los tipos como "Pendientes" (PE), si no existen ya. Para ello lee los diferentes tipos
de probabilidades activos y los combina, asignando un peso diferente a cada uno de ellos, hasta llegar a 1

Por ejemplo, con sólo un tipo sólo habría una posibilidad de combinación<br>
Con dos tipos, asumiendo mezclas de 0'2 en 0'2 tendríamos:
1'0A-0'0B, 0'8A-0'2B, 0'6A-0'4B, 0'4A-0'6B, 0'2A-0'8B, 0'0A-1'0B
