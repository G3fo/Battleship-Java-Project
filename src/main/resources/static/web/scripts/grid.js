/* Metodos propios de gridstack: 
all the functionalities are explained in the gridstack github
https://github.com/gridstack/gridstack.js/tree/develop/doc
*/

//Función principal que dispara el frame gridstack.js y carga la matriz con los barcos
const loadGrid = function(static) {
  var options = {
    //matriz 10 x 10
    width: 10,
    height: 10,
    //espacio entre las celdas (widgets)
    verticalMargin: 0,
    //altura de las celdas
    cellHeight: 45,
    //inhabilita la posibilidad de modificar el tamaño
    disableResize: true,
    //floating widgets
    float: true,
    //removeTimeout: tiempo en milisegundos antes de que el widget sea removido
    //mientras se arrastra fuera de la matriz (default: 2000)
    // removeTimeout:100,
    //permite al widget ocupar mas de una columna
    //sirve para no inhabilitar el movimiento en pantallas pequeñas
    disableOneColumnMode: true,
    // en falso permite arrastrar a los widget, true lo deniega
    staticGrid: static,
    //para animaciones
    animate: true
  };
  //inicializacion de la matriz
  $(".grid-stack").gridstack(options);

  grid = $("#grid").data("gridstack");

  //createGrid construye la estructura de la matriz
  createGrid(11, $(".grid-ships"), "ships");

  setShips();

  listenBusyCells("ships");
  $(".grid-stack").on("change", () => listenBusyCells("ships"));
};

//createGrid construye la estructura de la matriz
/*
size:refiere al tamaño de nuestra grilla (siempre sera una matriz
     de n*n, osea cuadrada)
element: es la tag que contendra nuestra matriz, para este ejemplo
        sera el primer div de nuestro body
id: sera como lo llamamos, en este caso ship ???)
*/

const createGrid = function(size, element, id) {
  // definimos un nuevo elemento: <div></div>
  let wrapper = document.createElement("DIV");

  // le agregamos la clase grid-wrapper: <div class="grid-wrapper"></div>
  wrapper.classList.add("grid-wrapper");

  //vamos armando la tabla fila por fila
  for (let i = 0; i < size; i++) {
    //row: <div></div>
    let row = document.createElement("DIV");
    //row: <div class="grid-row"></div>
    row.classList.add("grid-row");
    //row: <div id="ship-grid-row0" class="grid-wrapper"></div>
    row.id = `${id}-grid-row${i}`;
    /*
        wrapper:
                <div class="grid-wrapper">
                    <div id="ship-grid-row-0" class="grid-row">

                    </div>
                </div>
        */
    wrapper.appendChild(row);

    for (let j = 0; j < size; j++) {
      //cell: <div></div>
      let cell = document.createElement("DIV");
      //cell: <div class="grid-cell"></div>
      cell.classList.add("grid-cell");
      //aqui entran mis celdas que ocuparan los barcos
      if (i > 0 && j > 0) {
        //cell: <div class="grid-cell" id="ships00"></div>
        cell.id = `${id}${i - 1}${j - 1}`;
      }
      //aqui entran las celdas cabecera de cada fila
      if (j === 0 && i > 0) {
        // textNode: <span></span>
        let textNode = document.createElement("SPAN");
        /*String.fromCharCode(): método estático que devuelve 
                una cadena creada mediante el uso de una secuencia de
                valores Unicode especificada. 64 == @ pero al entrar
                cuando i sea mayor a cero, su primer valor devuelto 
                sera "A" (A==65)
                <span>A</span>*/
        textNode.innerText = String.fromCharCode(i + 64);
        //cell: <div class="grid-cell" id="ships00"></div>
        cell.appendChild(textNode);
      }
      // aqui entran las celdas cabecera de cada columna
      if (i === 0 && j > 0) {
        // textNode: <span>A</span>
        let textNode = document.createElement("SPAN");
        // 1
        textNode.innerText = j;
        //<span>1</span>
        cell.appendChild(textNode);
      }
      /*
            row:
                <div id="ship-grid-row0" class="grid-row">
                    <div class="grid-cell"></div>
                </div>
            */
      row.appendChild(cell);
    }
  }

  element.append(wrapper);
};

/*manejador de evento para rotar los barcos, el mismo se ejecuta al hacer click
sobre un barco
function(tipoDeBarco, celda)*/
const rotateShips = function(shipType, cells) {
  $(`#${shipType}`).click(function() {
    document.getElementById("alert-text").innerHTML = `Rotaste: ${shipType}`;
    console.log($(this));
    //Establecemos nuevos atributos para el widget/barco que giramos
    let x = +$(this).attr("data-gs-x");
    let y = +$(this).attr("data-gs-y");
    /*
        this hace referencia al elemento que dispara el evento (osea $(`#${shipType}`))
        .children es una propiedad de sólo lectura que retorna una HTMLCollection "viva"
        de los elementos hijos de un elemento.
        https://developer.mozilla.org/es/docs/Web/API/ParentNode/children
        El método .hasClass() devuelve verdadero si la clase existe como tal en el 
        elemento/tag incluso si tal elemento posee mas de una clase.
        https://api.jquery.com/hasClass/
        Consultamos si el barco que queremos girar esta en horizontal
        children consulta por el elemento contenido en "this"(tag que lanza el evento)
        ej:
        <div id="carrier" data-gs-x="0" data-gs-y="3" data-gs-width="5" 
        data-gs-height="1" class="grid-stack-item ui-draggable ui-resizable 
        ui-resizable-autohide ui-resizable-disabled">
            <div class="grid-stack-item-content carrierHorizontal ui-draggable-handle">
            </div>
            <div></div>
            <div class="ui-resizable-handle ui-resizable-se ui-icon 
            ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;">
            </div>
        </div>
        */
    if (
      $(this)
        .children()
        .hasClass(`${shipType}Horizontal`)
    ) {
      // grid.isAreaEmpty revisa si un array esta vacio**
      // grid.isAreaEmpty(fila, columna, ancho, alto)
      if (grid.isAreaEmpty(x, y + 1, 1, cells) || y + cells < 10) {
        if (y + cells - 1 < 10) {
          // grid.resize modifica el tamaño de un array(barco en este caso)**
          // grid.resize(elemento, ancho, alto)
          grid.resize($(this), 1, cells);
          $(this)
            .children()
            .removeClass(`${shipType}Horizontal`);
          $(this)
            .children()
            .addClass(`${shipType}Vertical`);
        } else {
          /* grid.update(elemento, fila, columna, ancho, alto)**
                        este metodo actualiza la posicion/tamaño del widget(barco)
                        ya que rotare el barco a vertical, no me interesa el ancho sino
                        el alto
                        */
          grid.update($(this), null, 10 - cells);
          grid.resize($(this), 1, cells);
          $(this)
            .children()
            .removeClass(`${shipType}Horizontal`);
          $(this)
            .children()
            .addClass(`${shipType}Vertical`);
        }
      } else {
        document.getElementById("alert-text").innerHTML =
          "A ship is blocking the way!";
      }

      //Este bloque se ejecuta si el barco que queremos girar esta en vertical
    } else {
      if (x + cells - 1 < 10) {
        grid.resize($(this), cells, 1);
        $(this)
          .children()
          .addClass(`${shipType}Horizontal`);
        $(this)
          .children()
          .removeClass(`${shipType}Vertical`);
      } else {
        /*en esta ocasion para el update me interesa el ancho y no el alto
                ya que estoy rotando a horizontal, por estoel tercer argumento no lo
                declaro (que es lo mismo que poner null o undefined)*/
        grid.update($(this), 10 - cells);
        grid.resize($(this), cells, 1);
        $(this)
          .children()
          .addClass(`${shipType}Horizontal`);
        $(this)
          .children()
          .removeClass(`${shipType}Vertical`);
      }
    }
  });
};

//Bucle que consulta por todas las celdas para ver si estan ocupadas o no
const listenBusyCells = function(id) {
  /* id vendria a ser ships. Recordar el id de las celdas del tablero se arma uniendo 
    la palabra ships + fila + columna contando desde 0. Asi la primer celda tendra id
    ships00 */
  for (let i = 0; i < 10; i++) {
    for (let j = 0; j < 10; j++) {
      if (!grid.isAreaEmpty(i, j)) {
        $(`#${id}${j}${i}`)
          .addClass("busy-cell")
          .removeClass("empty-cell");
      } else {
        $(`#${id}${j}${i}`)
          .removeClass("busy-cell")
          .addClass("empty-cell");
      }
    }
  }
};

const setShips = function() {
  for (i = 0; i < gamesJSON.ships.length; i++) {
    //Solo necesito la primera posicion, el resto de la informacion se deduce de la cantidad de celdas
    let shipType = gamesJSON.ships[i].shipType.toLowerCase();
    let x = +gamesJSON.ships[i].locations[0][1] - 1;
    let y = stringToInt(gamesJSON.ships[i].locations[0][0].toUpperCase());
    let w;
    let h;
    let orientation;

    if (
      gamesJSON.ships[i].locations[0][0] == gamesJSON.ships[i].locations[1][0]
    ) {
      w = gamesJSON.ships[i].locations.length;
      h = 1;
      orientation = "Horizontal";
    } else {
      h = gamesJSON.ships[i].locations.length;
      w = 1;
      orientation = "Vertical";
    }

    grid.addWidget(
      $(
        '<div id="' +
          shipType +
          '"><div class="grid-stack-item-content ' +
          shipType +
          orientation +
          '"></div><div/>'
      ),
      x,
      y,
      w,
      h
    );
  }
};

const setSalvoes = function() {
  // for (i = 0; i < gamesJSON.salvoes.length; i++) {
  //   var player;
  //   var opponent;
  //   let turn = gamesJSON.salvoes[i].turn;
  //   let player1 = gamesJSON.salvoes[i].game_player_id;
  //   for (let l = 0; l < gamesJSON.players.length; i++) {
  //     if (gamesJSON.players[l].gpid == gpId) {
  //       player = gamesJSON.players[l].gpid;
  //     } else {
  //       opponent = gamesJSON.players[l].gpid;
  //     }
  //   }
  //   for (j = 0; j < gamesJSON.salvoes[i].locations.length; j++) {
  //     let x = +gamesJSON.salvoes[i].locations[j].substring(1) - 1;
  //     let y = stringToInt(gamesJSON.salvoes[i].locations[j][0].toUpperCase());
  //     if (player1 == player) {
  //       document.getElementById("salvoes" + y + x).classList.add("sentSalvo");
  //     } else {
  //       document.getElementById("ships" + y + x).classList.add("sentSalvo");
  //     }
  //   }
  // if (gamesJSON.salvoes[i].hits) {
  //   for (k = 0; k < gamesJSON.salvoes[i].hits.length; k++) {
  //     x = +gamesJSON.salvoes[i].hits[k].substring(1) - 1;
  //     y = stringToInt(gamesJSON.salvoes[i].hits[k][0].toUpperCase());
  //     if (player1 == player) {
  //       document.getElementById("salvoes" + y + x).classList.remove("sentSalvo");
  //       document.getElementById("salvoes" + y + x).classList.add("ship-down");
  //     } else {
  //       document.getElementById("ships" + y + x).classList.remove("sentSalvo");
  //       document.getElementById("ships" + y + x).classList.add("ship-down");
  //     }
  //   }
  // }
  // }
};

const intToString = function(int) {
  switch (int) {
    case 0:
      return "A";
    case 1:
      return "B";
    case 2:
      return "C";
    case 3:
      return "D";
    case 4:
      return "E";
    case 5:
      return "F";
    case 6:
      return "G";
    case 7:
      return "H";
    case 8:
      return "I";
    case 9:
      return "J";
  }
};
