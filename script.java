const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const score1Element = document.getElementById('score1');
const score2Element = document.getElementById('score2');

canvas.width = 800;
canvas.height = 400;

// Load assets
const player1Sprite = new Image();
player1Sprite.src = 'assets/player1.png';

const player2Sprite = new Image();
player2Sprite.src = 'assets/player2.png';

const ballSprite = new Image();
ballSprite.src = 'assets/basketball.png';

const hoopSprite = new Image();
hoopSprite.src = 'assets/hoop.png';

// Game variables
let player1 = { x: 100, y: 300, width: 40, height: 60, vy: 0, jumping: false };
let player2 = { x: 660, y: 300, width: 40, height: 60, vy: 0, jumping: false };
let ball = { x: 400, y: 200, radius: 15, vx: 5, vy: 2 };
let hoop1 = { x: 50, y: 100, width: 20, height: 100 };
let hoop2 = { x: 730, y: 100, width: 20, height: 100 };

let score1 = 0, score2 = 0;
const gravity = 0.6;
const keys = {};

// Event listeners
window.addEventListener('keydown', (e) => keys[e.key] = true);
window.addEventListener('keyup', (e) => keys[e.key] = false);

// Helper functions
function drawRect(x, y, width, height, color) {
  ctx.fillStyle = color;
  ctx.fillRect(x, y, width, height);
}

function drawCircle(x, y, radius, image) {
  ctx.drawImage(image, x - radius, y - radius, radius * 2, radius * 2);
}

function drawText(text, x, y, color) {
  ctx.fillStyle = color;
  ctx.font = '20px Arial';
  ctx.fillText(text, x, y);
}

function applyGravity(player) {
  if (player.y + player.height < canvas.height) {
    player.vy += gravity;
  } else {
    player.vy = 0;
    player.jumping = false;
    player.y = canvas.height - player.height;
  }
}

function moveBall() {
  ball.x += ball.vx;
  ball.y += ball.vy;

  // Bounce off walls
  if (ball.x - ball.radius < 0 || ball.x + ball.radius > canvas.width) {
    ball.vx *= -1;
  }

  // Bounce off ground/ceiling
  if (ball.y - ball.radius < 0 || ball.y + ball.radius > canvas.height) {
    ball.vy *= -1;
  }

  // Check scoring
  if (ball.x > hoop1.x && ball.x < hoop1.x + hoop1.width && ball.y > hoop1.y && ball.y < hoop1.y + hoop1.height) {
    score2++;
    resetBall();
  } else if (ball.x > hoop2.x && ball.x < hoop2.x + hoop2.width && ball.y > hoop2.y && ball.y < hoop2.y + hoop2.height) {
    score1++;
    resetBall();
  }
}

function resetBall() {
  ball.x = canvas.width / 2;
  ball.y = canvas.height / 2;
  ball.vx = (Math.random() > 0.5 ? 5 : -5);
  ball.vy = (Math.random() > 0.5 ? 5 : -5);
}

// Game loop
function gameLoop() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // Draw players using images
  ctx.drawImage(player1Sprite, player1.x, player1.y, player1.width, player1.height);
  ctx.drawImage(player2Sprite, player2.x, player2.y, player2.width, player2.height);

  // Draw ball using image
  drawCircle(ball.x, ball.y, ball.radius, ballSprite);

  // Draw hoops using images
  ctx.drawImage(hoopSprite, hoop1.x, hoop1.y, hoop1.width, hoop1.height);
  ctx.drawImage(hoopSprite, hoop2.x, hoop2.y, hoop2.width, hoop2.height);

  // Draw scores
  drawText(`Player 1: ${score1}`, 50, 30, 'black');
  drawText(`Player 2: ${score2}`, canvas.width - 150, 30, 'black');

  // Player 1 controls
  if (keys['w'] && !player1.jumping) {
    player1.vy = -12;
    player1.jumping = true;
  }
  if (keys['a']) player1.x -= 5;
  if (keys['d']) player1.x += 5;

  // Player 2 controls
  if (keys['ArrowUp'] && !player2.jumping) {
    player2.vy = -12;
    player2.jumping = true;
  }
  if (keys['ArrowLeft']) player2.x -= 5;
  if (keys['ArrowRight']) player2.x += 5;

  // Apply gravity
  applyGravity(player1);
  applyGravity(player2);

  // Move players
  player1.y += player1.vy;
  player2.y += player2.vy;

  // Move ball
  moveBall();

  // Update scores on screen
  score1Element.innerText = score1;
  score2Element.innerText = score2;

  requestAnimationFrame(gameLoop);
}

// Start game when assets are loaded
player1Sprite.onload = function() {
  player2Sprite.onload = function() {
    ballSprite.onload = function() {
      hoopSprite.onload = function() {
        gameLoop(); // Start the game loop after all images are loaded
      }
    }
  }
};
