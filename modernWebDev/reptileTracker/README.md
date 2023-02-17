# Reptile Tracker

## Installing Dependencies

With yarn

```bash
yarn
```

With npm

```bash
npm install
```

## Setup

### .env

Copy the contents of `.env.example` into a new file called `.env`.

### Database

With yarn
Create the database by running

```bash
yarn db:migrate
```

With npm

```bash
npm run db:migrate
```

You will need the re-run this command anytime you make changes to the schema file.

### Running the server

Start the server by running:

With yarn

```bash
yarn dev
```

With npm

```bash
npm run dev
```
