window.addEventListener('load', () => {
    const contenedor_loader = document.querySelector('.contenedor')
    contenedor_loader.style.opacity = 0
    contenedor_loader.style.visibility = 'hidden'
})

const app = Vue.createApp({
    data() {
        return {
            cliente: [],
            tarjetas: [],
            typeCard: [],
            typeColor: [],
        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.cliente = res.data
                this.tarjetas = res.data.cards

                console.log(this.cliente)
                console.log(this.tarjetas)
            })
            .catch(err => console.log(err))
    },
    methods: {
        signOut() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = "/index.html"
                })
        },
        createCard() {
            axios.post("/api/clients/current/cards", "type=" + this.typeCard + "&color=" + this.typeColor, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    swal({
                        title: "Congratulations",
                        text: "You have a new card",
                        icon: "success",
                        button: "OK",
                    })
                    location.href = "/card.html"
                })
                .catch(err => {
                    swal({
                        title: "Alert",
                        text: "You have the max cards, contact customer service",
                        icon: "warning",
                    });
                })
        }

    },
    computed: {},
})
app.mount("#app")