# Readme
Idea:<br>
1. Leer los datos de los sorteos y registrarlos en la base de datos<br><br>
2. Proceso para calcular estadísticas de cada tipo posible y guardarlas en BBDD hasta una fecha determinada.
   Para ello se basa en la lista de sorteos y el micro define los diferentes tipos a tener en cuenta<br><br>
3. Proceso que genera combinaciones entre tipos, asignando un peso variable a cada tipo que aparece en la combinación.
   Ejemplos (Tipo - %): (A - 40%, B - 60%), (A - 100%), (B - 60%, C - 20%, D = 20%)<br>
   Además, calcula y guarda las probabilidades combinadas para cada uno de los sorteos<br><br>
4. Proceso para generar sorteos a partir de las estadísticas guardadas y los últimos sorteos<br>
   4.1. Utilizar combinaciones entre los tipos de estadísticas. Por ejemplo: 10% aleatorio, 90% último sorteo; 10% aleatorio, 70% último sorteo, 20% "le toca" porque hace mucho que no sale<br>
5. Proceso que llama a 2 tantas veces como sea necesario para acertar al menos 3 números. Se repite durante varios sorteos distintos
- La idea es ver tras, por ejemplo, 100 sorteos (100 repeticiones), cuál es la mejor combinación de estadísticas.

## Definición
Proceso que calcula las probabilidades asociadas por tipo de cálculo en una fecha concreta.
Para ello precisa de la lista de sorteos y se definen los tipos. Por ejemplo, posibles tipos de cálculos
- Números que más veces han salido en los últimos n sorteos -> En algún sitio he leído algo que indicaba que era buena idea tener los que más veces han salido de los últimos 2 años
- Números que más veces han salido en los últimos n sorteos
- Números que menos veces han salido (totales y n sorteos)
- Números según su orden de aparición, siendo más probable cuanto más tiempo hace que no sale -> HigherWhenLastAppearanceIsOlder
- Aleatorio
- Combinaciones de apariciones:
    - Último sorteo
    - Dos últimos sorteos
    - etc

#########################################################<br>
ESTE MICRO IMPLEMENTA EL PASO 2<br>
#########################################################<br>
