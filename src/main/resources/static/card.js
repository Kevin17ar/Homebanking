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
            cardSelect: "",
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
                    location.href = "http://localhost:8686/index.html"
                })
        },
        cardMax() {
            swal({
                title: "Alert",
                text: "You have the max cards, contact customer service",
                icon: "warning",
            });
        },
        deleteCard() {
            swal({
                    title: "Are you sure?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                })
                .then((willDelete) => {
                    if (willDelete) {
                        axios.post('/api/clients/current/card', "id=" + this.cardSelect)
                            .then(response => {
                                swal({
                                    title: "Congratulations",
                                    text: "card delete",
                                    icon: "success",
                                    button: "OK",
                                });
                                // location.reload();
                            })
                            .then(res => location.reload())
                            .catch(err => {
                                swal({
                                    title: "Alert",
                                    text: "Check the data entered",
                                    icon: "warning",
                                })
                            })
                    } else {
                        swal("for question contac us");
                    }
                });
        },
    },
    computed: {
        cardDebit() {
            return this.tarjetas.filter(tarjeta => tarjeta.type === "DEBIT")
        },
        cardCredit() {
            return this.tarjetas.filter(tarjeta => tarjeta.type == "CREDIT")
        },

    },
})
app.mount("#app")